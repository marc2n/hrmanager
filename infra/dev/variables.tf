variable "resource_group_name" {
  description = "Existing application resource group created by bootstrap."
  type        = string
  default     = "rg-hrmanager-app-dev"
}

variable "project_name" {
  description = "Short project name used in resource."
  type        = string
  default     = "hrmanager"
}

variable "container_registry_name" {
  description = "Globally unique name of the dev container registry."
  type        = string
  default     = "acrmarc2nhrmanagertfdev"

  validation {
    condition     = can(regex("^[a-z0-9]{5,50}$", var.container_registry_name))
    error_message = "Use 5–50 lowercase letters or numbers."
  }
}

variable "postgres_server_name" {
  description = "Globally unique PostgreSQL server name."
  type        = string
  default     = "psql-marc2n-hrmanager-dev"
}

variable "postgres_admin_password" {
  description = "Administrator password supplied through GitHub Secrets."
  type        = string
  sensitive   = true
}

variable "application_image" {
  description = "Exact application image to deploy."
  type        = string
  default     = "acrmarc2nhrmanagertfdev.azurecr.io/hr-app@sha256:f84bf91fba51e2ef76b810f6ad766ab31e740ea71eee0f150b00624d09c163ed"
}