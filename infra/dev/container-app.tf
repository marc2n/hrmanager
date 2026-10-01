resource "azurerm_container_app" "app" {
  name                         = "ca-hrmanager-dev"
  resource_group_name          = data.azurerm_resource_group.app.name
  container_app_environment_id = azurerm_container_app_environment.app.id

  revision_mode         = "Single"
  workload_profile_name = "Consumption"

  identity {
    type         = "UserAssigned"
    identity_ids = [azurerm_user_assigned_identity.app_runtime.id]
  }

  registry {
    server   = azurerm_container_registry.app.login_server
    identity = azurerm_user_assigned_identity.app_runtime.id
  }

  secret {
    name  = "postgres-password"
    value = var.postgres_admin_password
  }

  ingress {
    external_enabled           = true
    target_port                = 8080
    transport                  = "auto"
    allow_insecure_connections = false

    traffic_weight {
      latest_revision = true
      percentage      = 100
    }
  }

  template {
    min_replicas = 1
    max_replicas = 1

    container {
      name   = "hr-app"
      image  = var.application_image
      cpu    = 0.5
      memory = "1Gi"

      env {
        name  = "SPRING_PROFILES_ACTIVE"
        value = "azure"
      }

      env {
        name  = "DB_HOST"
        value = azurerm_postgresql_flexible_server.app.fqdn
      }

      env {
        name  = "DB_NAME"
        value = azurerm_postgresql_flexible_server_database.app.name
      }

      env {
        name  = "DB_USERNAME"
        value = azurerm_postgresql_flexible_server.app.administrator_login
      }

      env {
        name        = "DB_PASSWORD"
        secret_name = "postgres-password"
      }

      startup_probe {
        transport               = "HTTP"
        port                    = 8080
        path                    = "/actuator/health/liveness"
        interval_seconds        = 10
        timeout                 = 5
        failure_count_threshold = 60
      }

      liveness_probe {
        transport               = "HTTP"
        port                    = 8080
        path                    = "/actuator/health/liveness"
        interval_seconds        = 10
        timeout                 = 5
        failure_count_threshold = 3
      }

      readiness_probe {
        transport               = "HTTP"
        port                    = 8080
        path                    = "/actuator/health/readiness"
        interval_seconds        = 10
        timeout                 = 5
        failure_count_threshold = 3
        success_count_threshold = 1
      }
    }
  }

  depends_on = [
    azurerm_postgresql_flexible_server_firewall_rule.azure_services
  ]

  tags = local.tags

  lifecycle {
    ignore_changes = [
      template[0].container[0].image
    ]
  }
}