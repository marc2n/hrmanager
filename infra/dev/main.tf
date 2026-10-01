locals {
  tags = {
    project     = var.project_name
    environment = "dev"
    managed_by  = "terraform"
  }
}

data "azurerm_resource_group" "app" {
  name = var.resource_group_name
}