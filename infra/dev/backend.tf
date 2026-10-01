terraform {
  backend "azurerm" {
    storage_account_name = "stmarc2nhrmanagertfdev"
    container_name       = "tfstate"
    key                  = "infrastructure-dev.tfstate"

    use_azuread_auth = true
  }
}