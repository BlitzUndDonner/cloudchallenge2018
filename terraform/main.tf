provider "google" {
  project = "cloud-hackathon-team-athena"
  region  = "europe-west1"
  zone    = "europe-west1-c"
}

resource "google_bigquery_dataset" "default" {
  dataset_id                  = "test_dataset"
  friendly_name               = "test_dataset"
  description                 = "This is a test description"
  location                    = "EU"
  default_table_expiration_ms = 3600000
}

resource "google_bigquery_table" "default" {
  dataset_id = "${google_bigquery_dataset.default.dataset_id}"
  table_id   = "bar"
  
  time_partitioning {
    field = "timestamp"
    type = "DAY"    
  }

  schema = "${file("bigquery_schema.json")}"
}
