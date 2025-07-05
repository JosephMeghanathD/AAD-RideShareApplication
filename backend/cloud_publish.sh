#!/bin/sh

# This script builds and pushes Docker images for the microservices.
#
# Usage:
#   ./cloud-build.sh          (Builds and pushes ALL services)
#   ./cloud-build.sh --auth   (Builds and pushes ONLY auth-service)
#   ./cloud-build.sh --ride   (Builds and pushes ONLY ride-service)
#   ./cloud-build.sh --chat   (Builds and pushes ONLY chat-service)
#

# Exit immediately if a command exits with a non-zero status.
set -e

# --- Configuration ---
# The script will use environment variables for PROJECT_ID and REGION.
if [ -z "$PROJECT_ID" ] || [ -z "$REGION" ]; then
  echo "Error: Please set the PROJECT_ID and REGION environment variables."
  exit 1
fi

# --- Argument Parsing ---
# Define the default list of services.
SERVICES_TO_BUILD=("auth-service" "ride-service" "chat-service")

# Check if a command-line argument (like --auth) was provided.
if [ -n "$1" ]; then
  case "$1" in
    --auth)
      SERVICES_TO_BUILD=("auth-service")
      echo "✅ Targeted build: Preparing to build ONLY auth-service."
      ;;
    --ride)
      SERVICES_TO_BUILD=("ride-service")
      echo "✅ Targeted build: Preparing to build ONLY ride-service."
      ;;
    --chat)
      SERVICES_TO_BUILD=("chat-service")
      echo "✅ Targeted build: Preparing to build ONLY chat-service."
      ;;
    *)
      echo "Error: Invalid argument '$1'."
      echo "Usage: $0 [--auth | --ride | --chat]"
      exit 1
      ;;
  esac
else
    echo "✅ Default build: Preparing to build ALL services."
fi

# --- Shared Library Build ---
# This needs to be built regardless, as other services depend on it.
echo "--- Building shared library (RideShareApp) ---"
cd ./RideShareApp || exit
./gradlew clean build publishToMavenLocal
cd ..

# --- Main Build Loop ---
TAG="latest"

# Loop through the list of services determined by the script arguments.
for SERVICE in "${SERVICES_TO_BUILD[@]}"
do
  echo ""
  echo "======================================================="
  echo "--- Building and Pushing: $SERVICE for linux/amd64 ---"
  echo "======================================================="

  # Determine the directory for the service.
  SERVICE_DIR=""
  case $SERVICE in
    auth-service) SERVICE_DIR="RideShareAppAuthService" ;;
    ride-service) SERVICE_DIR="RideShareAppRideService" ;;
    chat-service) SERVICE_DIR="RideShareAppChatService" ;;
  esac

  cd ./$SERVICE_DIR || exit

  echo "--> Running ./gradlew build for $SERVICE"
  ./gradlew clean build

  IMAGE_NAME="${REGION}-docker.pkg.dev/${PROJECT_ID}/ride-share-repo/${SERVICE}:${TAG}"

  # --- Corrected Build & Push Command ---
  # Use 'docker buildx' to build for the target platform (amd64) and push in one step.
  echo "--> Building and pushing Docker image: ${IMAGE_NAME}"
  docker buildx build --platform linux/amd64 -t "${IMAGE_NAME}" --push .

  cd ..
  echo "--- Finished $SERVICE ---"
done

echo ""
echo "✅ Build and push process completed successfully."