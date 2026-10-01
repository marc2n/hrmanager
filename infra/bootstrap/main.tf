locals {
  tags = {
    project     = var.project_name
    environment = "dev"
    managed_by  = "terraform"
  }
}

resource "azurerm_resource_group" "bootstrap" {
  location = var.location
  name     = "rg-${var.project_name}-bootstrap-dev"
  tags     = local.tags
}

resource "azurerm_resource_group" "app" {
  location = var.location
  name     = "rg-${var.project_name}-app-dev"
  tags     = local.tags
}