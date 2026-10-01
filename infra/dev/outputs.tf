output "resource_group_name" {
  description = "Application resource group used by this configuration."
  value       = data.azurerm_resource_group.app.name
}

output "location" {
  description = "Region of the application resource group."
  value       = data.azurerm_resource_group.app.location
}