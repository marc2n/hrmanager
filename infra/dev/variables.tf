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