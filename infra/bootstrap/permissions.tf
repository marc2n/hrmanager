resource "azurerm_role_assignment" "github_infra_resources" {
  principal_id         = azurerm_user_assigned_identity.github_infra.principal_id
  principal_type       = "ServicePrincipal"
  scope                = azurerm_resource_group.app.id
  role_definition_name = "Contributor"
}

resource "azurerm_role_assignment" "github_infra_state" {
  principal_id         = azurerm_user_assigned_identity.github_infra.principal_id
  scope                = azurerm_storage_container.state.id
  principal_type       = "ServicePrincipal"
  role_definition_name = "Storage Blob Data Contributor"
}

resource "azurerm_role_assignment" "github_app_reader" {
  principal_id         = azurerm_user_assigned_identity.github_app.principal_id
  principal_type       = "ServicePrincipal"
  role_definition_name = "Reader"
  scope                = azurerm_resource_group.app.id
}

resource "azurerm_role_assignment" "github_app_acr_push" {
  count = var.container_registry_id != null ? 1 : 0

  scope                = var.container_registry_id
  role_definition_name = "AcrPush"
  principal_id         = azurerm_user_assigned_identity.github_app.principal_id
  principal_type       = "ServicePrincipal"
}

resource "azurerm_role_assignment" "runtime_acr_pull" {
  count = (var.container_registry_id != null && var.runtime_identity_principal_id != null) ? 1 : 0

  scope                = var.container_registry_id
  role_definition_name = "AcrPull"
  principal_id         = var.runtime_identity_principal_id
  principal_type       = "ServicePrincipal"
}

resource "azurerm_role_assignment" "github_app_deployment" {
  scope                = azurerm_resource_group.app.id
  role_definition_name = "Container Apps Contributor"
  principal_id         = azurerm_user_assigned_identity.github_app.principal_id
  principal_type       = "ServicePrincipal"
}

resource "azurerm_role_assignment" "github_app_runtime_identity" {
  count = var.runtime_identity_id != null ? 1 : 0

  scope                = var.runtime_identity_id
  role_definition_name = "Managed Identity Operator"
  principal_id         = azurerm_user_assigned_identity.github_app.principal_id
  principal_type       = "ServicePrincipal"
}