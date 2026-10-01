resource "azurerm_user_assigned_identity" "github_infra" {
  location            = azurerm_resource_group.bootstrap.location
  name                = "id-${var.project_name}-github-infra-dev"
  resource_group_name = azurerm_resource_group.bootstrap.name

  tags = local.tags
}

resource "azurerm_user_assigned_identity" "github_app" {
  location            = azurerm_resource_group.bootstrap.location
  name                = "id-${var.project_name}-github-app-dev"
  resource_group_name = azurerm_resource_group.bootstrap.name

  tags = local.tags
}