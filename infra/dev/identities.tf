resource "azurerm_user_assigned_identity" "app_runtime" {
  name                = "id-hrmanager-runtime-dev"
  resource_group_name = data.azurerm_resource_group.app.name
  location            = data.azurerm_resource_group.app.location

  tags = local.tags
}