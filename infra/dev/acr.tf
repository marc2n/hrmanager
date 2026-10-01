resource "azurerm_container_registry" "app" {
  name                = var.container_registry_name
  resource_group_name = data.azurerm_resource_group.app.name
  location            = data.azurerm_resource_group.app.location
  sku                 = "Basic"

  admin_enabled                 = false
  public_network_access_enabled = true
  role_assignment_mode          = "LegacyRegistryPermissions"

  tags = local.tags
}