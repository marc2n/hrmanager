variable "subscription_id" {
  description = "Azure subscription used for the HR dev deployment."
  type        = string
}

variable "location" {
  description = "Azure region for the deployment."
  type        = string
  default     = "francecentral"
}

variable "project_name" {
  description = "Short project name used in resource names."
  type        = string
  default     = "hrmanager"
}

variable "state_storage_account_name" {
  description = "Globally unique storage account name for Terraform state."
  type        = string

  validation {
    condition = can(
      regex("^[a-z0-9]{3,24}$", var.state_storage_account_name)
    )
    error_message = "Use 3–24 lowercase letters and numbers only."
  }
}

variable "container_registry_id" {
  description = "Existing dev registry resource ID. Set after the registry is deployed."
  type        = string
  default     = null
}

variable "runtime_identity_principal_id" {
  description = "Principal ID of the Container App runtime identity."
  type        = string
  default     = null
}