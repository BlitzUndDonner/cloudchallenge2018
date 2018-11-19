variable "region" {
  type = "string"
  default = "europe-north1"
}

variable "dataset" {
  type = "string"
  default = "europe_north1"
}

variable "zone" {
  type = "string"
  default = "europe-north1-a"
}

variable "project" {
  default = "cloud-hackathon-team-athena"
}

provider "google" {
  project = "${var.project}"
  region  = "${var.region}"
  zone    = "${var.zone}"
}


resource "google_bigquery_dataset" "default" {
  dataset_id                  = "${var.dataset}"
  friendly_name               = "${var.dataset}"
  description                 = "This is a test description"
  location                    = "EU"
  default_table_expiration_ms = 3600000
}

resource "google_cloudbuild_trigger" "build_trigger_counters" {
  project = "${var.project}"
  trigger_template {
    branch_name = "master"
    project = "${var.project}"
    repo_name = "counter_function"
  }
  description = "Counter service ${var.region}"
  filename = "cloudbuild.yaml"
    substitutions = {
    _DATASET_NAME = "${var.dataset}"
  }
}


resource "google_cloudbuild_trigger" "build_trigger_dataflow" {
  project = "${var.project}"
  trigger_template {
    branch_name = "master"
    project = "${var.project}"
    repo_name = "dataflow"
  }
  description = "DataFlow pipeline ${var.region}"
  filename = "cloudbuild.yaml"
    substitutions = {
    _REGION_NAME = "${var.region}"
  }
}

