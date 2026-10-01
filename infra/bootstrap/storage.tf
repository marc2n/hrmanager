resource "azurerm_storage_account" "state" {
  account_replication_type = "LRS"
  account_tier             = "Standard"
  account_kind             = "StorageV2"
  location                 = azurerm_resource_group.bootstrap.location
  name                     = var.state_storage_account_name
  resource_group_name      = azurerm_resource_group.bootstrap.name

  https_traffic_only_enabled      = true
  min_tls_version                 = "TLS1_2"
  public_network_access_enabled   = true
  allow_nested_items_to_be_public = false
  shared_access_key_enabled       = false
  default_to_oauth_authentication = true

  blob_properties {
    versioning_enabled = true

    delete_retention_policy {
      days = 7
    }

    container_delete_retention_policy {
      days = 7
    }
  }

  tags = local.tags
}

data "azurerm_client_config" "current" {}

resource "azurerm_role_assignment" "bootstrap_state_access" {
  principal_id         = data.azurerm_client_config.current.object_id
  scope                = azurerm_storage_account.state.id
  role_definition_name = "Storage Blob Data Contributor"
}

resource "azurerm_storage_container" "state" {
  name                  = "tfstate"
  storage_account_id    = azurerm_storage_account.state.id
  container_access_type = "private"

  depends_on = [
    azurerm_role_assignment.bootstrap_state_access
  ]
}

resource "azurerm_storage_container" "bootstrap_state" {
  name                  = "tfstate-bootstrap"
  storage_account_id    = azurerm_storage_account.state.id
  container_access_type = "private"
  depends_on = [
    azurerm_role_assignment.bootstrap_state_access
  ]
}