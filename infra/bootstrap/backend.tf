terraform {
  backend "azurerm" {
    storage_account_name = "stmarc2nhrmanagertfdev"
    container_name       = "tfstate-bootstrap"
    key                  = "bootstrap.tfstate"

    use_azuread_auth = true
    use_cli          = true
  }
}