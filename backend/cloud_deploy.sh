#!/bin/sh

# This script securely deploys microservices to Google Cloud Run.
#
# Usage:
#   ./cloud-deploy-secure.sh          (Deploys ALL services)
#   ./cloud-deploy-secure.sh --auth   (Deploys ONLY auth-service)
#   ./cloud-deploy-secure.sh --ride   (Deploys ONLY ride-service)
#   ./cloud-deploy-secure.sh --chat   (Deploys ONLY chat-service)
#
# It should be run AFTER service accounts have been created and all
# necessary IAM permissions have been granted.

# Exit immediately if a command exits with a non-zero status.
set -e

# --- Configuration Check ---
if [ -z "$PROJECT_ID" ] || [ -z "$REGION" ]; then
  echo "Error: Please set the PROJECT_ID and REGION environment variables first."
  echo "Example: export PROJECT_ID=\"your-gcp-project-id\""
  echo "         export REGION=\"us-central1\""
  exit 1
fi

# --- Argument Parsing ---
# Define the default list of services to deploy.
SERVICES_TO_DEPLOY=("auth-service" "ride-service" "chat-service")

# Check if a command-line argument (e.g. --auth) was provided to target a single service.
if [ -n "$1" ]; then
  case "$1" in
    --auth)
      SERVICES_TO_DEPLOY=("auth-service")
      echo "✅ Targeted deployment: Preparing to deploy ONLY auth-service."
      ;;
    --ride)
      SERVICES_TO_DEPLOY=("ride-service")
      echo "✅ Targeted deployment: Preparing to deploy ONLY ride-service."
      ;;
    --chat)
      SERVICES_TO_DEPLOY=("chat-service")
      echo "✅ Targeted deployment: Preparing to deploy ONLY chat-service."
      ;;
    *)
      echo "Error: Invalid argument '$1'."
      echo "Usage: $0 [--auth | --ride | --chat]"
      exit 1
      ;;
  esac
else
    echo "✅ Default deployment: Preparing to deploy ALL services."
fi

echo "Deploying to Project: '$PROJECT_ID' in Region: '$REGION'"
echo "---"

# --- Main Deployment Loop ---
# Loop through the list of services determined by the script arguments.
for SERVICE in "${SERVICES_TO_DEPLOY[@]}"
do
  echo ""
  echo "============================================="
  echo "--- Securely Deploying: $SERVICE ---"
  echo "============================================="

  # Determine service-specific configuration using a case statement.
  PORT=""
  SERVICE_ACCOUNT_EMAIL=""
  case $SERVICE in
    auth-service)
      PORT="8080"
      SERVICE_ACCOUNT_EMAIL="auth-service-sa@${PROJECT_ID}.iam.gserviceaccount.com"
      ;;
    ride-service)
      PORT="8081"
      SERVICE_ACCOUNT_EMAIL="ride-service-sa@${PROJECT_ID}.iam.gserviceaccount.com"
      ;;
    chat-service)
      PORT="8082"
      SERVICE_ACCOUNT_EMAIL="chat-service-sa@${PROJECT_ID}.iam.gserviceaccount.com"
      ;;
    *)
      echo "Error: Unknown service '$SERVICE' in the case statement. No configuration defined."
      exit 1
      ;;
  esac

  # Construct and execute the SECURE deployment command for the current service.
  gcloud run deploy "$SERVICE" \
    --image="${REGION}-docker.pkg.dev/${PROJECT_ID}/ride-share-repo/${SERVICE}:latest" \
    --service-account="$SERVICE_ACCOUNT_EMAIL" \
    --port="$PORT" \
    --ingress=all \
    --allow-unauthenticated \
    --region="$REGION"
done

echo ""
echo "============================================="
echo "✅ Deployment process completed successfully."
echo "You can view the services in the GCP Console:"
echo "https://console.cloud.google.com/run?project=${PROJECT_ID}"
echo "============================================="