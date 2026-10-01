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

output "postgres_server_name" {
  description = "Name of the PostgreSQL server."
  value       = azurerm_postgresql_flexible_server.app.name
}

output "postgres_host" {
  description = "PostgreSQL hostname for the application datasource."
  value       = azurerm_postgresql_flexible_server.app.fqdn
}

output "postgres_database_name" {
  description = "Application database name."
  value       = azurerm_postgresql_flexible_server_database.app.name
}

output "postgres_username" {
  description = "Database login for the initial dev deployment."
  value       = azurerm_postgresql_flexible_server.app.administrator_login
}