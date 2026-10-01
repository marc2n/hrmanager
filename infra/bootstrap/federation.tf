resource "azurerm_federated_identity_credential" "github_infra" {
  name                      = "github-dev-infra"
  audience                  = ["api://AzureADTokenExchange"]
  issuer                    = "https://token.actions.githubusercontent.com"
  subject                   = "repo:marc2n@14019628/hrmanager@1397137039:environment:dev-infra"
  user_assigned_identity_id = azurerm_user_assigned_identity.github_infra.id
}

resource "azurerm_federated_identity_credential" "github_app" {
  name                      = "github-dev-app"
  audience                  = ["api://AzureADTokenExchange"]
  issuer                    = "https://token.actions.githubusercontent.com"
  subject                   = "repo:marc2n@14019628/hrmanager@1397137039:environment:dev-app"
  user_assigned_identity_id = azurerm_user_assigned_identity.github_app.id
}