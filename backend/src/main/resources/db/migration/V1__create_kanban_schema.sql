-- ============================================================
--  V1 – Initial Kanban schema
--  DB: kanban_db
-- ============================================================

-- ── Workspaces ────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS workspaces (
    id          UUID        PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ  NOT NULL DEFAULT now()
);

-- ── Boards ────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS boards (
    id           UUID        PRIMARY KEY,
    workspace_id UUID        NOT NULL REFERENCES workspaces (id) ON DELETE CASCADE,
    name         VARCHAR(255) NOT NULL,
    description  TEXT,
    created_at   TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at   TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_boards_workspace_id ON boards (workspace_id);

-- ── Columns ───────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS columns (
    id         UUID        PRIMARY KEY,
    board_id   UUID        NOT NULL REFERENCES boards (id) ON DELETE CASCADE,
    name       VARCHAR(255) NOT NULL,
    position   INT         NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_columns_board_id ON columns (board_id);

-- ── Cards ─────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS cards (
    id          UUID        PRIMARY KEY,
    column_id   UUID        NOT NULL REFERENCES columns (id) ON DELETE CASCADE,
    title       VARCHAR(255) NOT NULL,
    description TEXT,
    position    INT         NOT NULL DEFAULT 0,
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_cards_column_id ON cards (column_id);
