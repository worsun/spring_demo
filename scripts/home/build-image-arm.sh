#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)
PROJECT_ROOT=$(cd "${SCRIPT_DIR}/../.." && pwd)
cd "${PROJECT_ROOT}/server/home"

# 清理临时目录以避免 protobuf 插件问题
echo "清理临时目录..."
rm -rf target/temp-protoc target/protoc-dependencies target/generated-sources/protobuf
mkdir -p target/temp-protoc

# Try to build with protobuf first
MVN_CMD=${MVN_CMD:-mvn}
IMAGE_NAME=${IMAGE_NAME:-home-service-arm}
IMAGE_TAG=${IMAGE_TAG:-latest}
FULL_IMAGE_NAME="${IMAGE_NAME}:${IMAGE_TAG}"

echo "[1/2] Building application artifact with ${MVN_CMD}..."
if ! ${MVN_CMD} -B clean package -DskipTests; then
  echo "Protobuf build failed, trying to build without protobuf plugin..."
  # If protobuf build fails, try to build without running the protobuf plugin
  ${MVN_CMD} -B clean compile -DskipTests
  ${MVN_CMD} -B package -DskipTests -Dmaven.test.skip=true -Dprotoc.skip=true
fi

JAR_FILE=$(find target -maxdepth 1 -type f -name '*.jar' ! -name 'original-*' | head -n 1)
if [[ -z "${JAR_FILE}" ]]; then
  echo "无法在 target 目录中找到可用的 jar 包" >&2
  exit 1
fi

echo "[2/2] Building ARM Docker image ${FULL_IMAGE_NAME} (using ${JAR_FILE})..."
docker build --no-cache --pull=false -f Dockerfile.arm --build-arg JAR_FILE="${JAR_FILE}" -t "${FULL_IMAGE_NAME}" .

echo "ARM镜像构建完成：${FULL_IMAGE_NAME}"