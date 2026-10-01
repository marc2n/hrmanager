output "bootstrap_resource_group_name" {
  description = "Resource group containing deployment prerequisites."
  value       = azurerm_resource_group.bootstrap.name
}

output "application_resource_group_name" {
  description = "Resource group for application infrastructure."
  value       = azurerm_resource_group.app.name
}

output "state_storage_account_name" {
  description = "Storage account used for Terraform state."
  value       = azurerm_storage_account.state.name
}

output "state_container_name" {
  description = "Blob container used for Terraform state."
  value       = azurerm_storage_container.state.name
}

output "github_infra_client_id" {
  description = "Client ID used by the GitHub infrastructure workflow."
  value       = azurerm_user_assigned_identity.github_infra.client_id
}

output "github_app_client_id" {
  description = "Client ID used by the GitHub application workflow."
  value       = azurerm_user_assigned_identity.github_app.client_id
}

output "tenant_id" {
  description = "Microsoft Entra tenant containing the identities."
  value       = data.azurerm_client_config.current.tenant_id
}

output "bootstrap_state_container_name" {
  description = "Container used exclusively for bootstrap Terraform state."
  value       = azurerm_storage_container.bootstrap_state.name
}