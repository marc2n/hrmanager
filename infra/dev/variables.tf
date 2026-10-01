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