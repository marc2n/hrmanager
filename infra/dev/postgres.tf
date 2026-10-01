resource "azurerm_postgresql_flexible_server" "app" {
  name                = var.postgres_server_name
  resource_group_name = data.azurerm_resource_group.app.name
  location            = data.azurerm_resource_group.app.location

  version    = "18"
  sku_name   = "B_Standard_B1ms"
  storage_mb = 32768

  administrator_login    = "hradmin"
  administrator_password = var.postgres_admin_password

  public_network_access_enabled = true
  backup_retention_days         = 7
  geo_redundant_backup_enabled  = false

  authentication {
    password_auth_enabled         = true
    active_directory_auth_enabled = false
  }

  tags = local.tags

  lifecycle {
    ignore_changes = [zone]
  }
}

resource "azurerm_postgresql_flexible_server_database" "app" {
  name      = "hr"
  server_id = azurerm_postgresql_flexible_server.app.id
  charset   = "UTF8"
  collation = "en_US.utf8"
}

resource "azurerm_postgresql_flexible_server_firewall_rule" "azure_services" {
  name             = "AllowAzureServices"
  server_id        = azurerm_postgresql_flexible_server.app.id
  start_ip_address = "0.0.0.0"
  end_ip_address   = "0.0.0.0"
}