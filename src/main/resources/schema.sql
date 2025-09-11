-- Esquema inicial para H2 (memoria)
-- Crea la tabla si no existe (coincide con com.plexus.infraestructure.persistance.entity.AssetEntity)

CREATE TABLE IF NOT EXISTS assets (
    id VARCHAR(64) PRIMARY KEY,
    filename VARCHAR(255) NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    url VARCHAR(512),
    size BIGINT,
    upload_date TIMESTAMP WITH TIME ZONE
);

-- Índices opcionales
CREATE INDEX IF NOT EXISTS idx_assets_filename ON assets(filename);
CREATE INDEX IF NOT EXISTS idx_assets_upload_date ON assets(upload_date);


