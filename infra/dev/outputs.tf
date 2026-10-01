output "resource_group_name" {
  description = "Application resource group used by this configuration."
  value       = data.azurerm_resource_group.app.name
}

output "location" {
  description = "Region of the application resource group."
  value       = data.azurerm_resource_group.app.location
}

output "container_registry_name" {
  description = "Name of the application container registry."
  value       = azurerm_container_registry.app.name
}

output "container_registry_id" {
  description = "Resource ID used to scope registry role assignments."
  value       = azurerm_container_registry.app.id
}

output "container_registry_login_server" {
  description = "Registry hostname used for Docker login and image tags."
  value       = azurerm_container_registry.app.login_server
}