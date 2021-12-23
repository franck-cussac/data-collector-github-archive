# Collect data from GitHub archive

This project is designed to run in Scaleway cloud with an S3 bucket in Paris.

## Build
Using Docker with buildkit's plugin enabled, you can build a jar localy with the following command :
```
DOCKER_BUILDKIT=1 docker build -o target/ -f build.Dockerfile .
```

## Deploy
You can build and deploy a container image in DockerHub running these commands :
```
docker build -t <namespace/image-name:tag> .
docker push <namespace/image-name:tag>
```

## Run
If you want to run your version of data collector, you can edit the pod.yaml file with your image name :
```yaml
apiVersion: v1
kind: Pod
metadata:
  labels:
    run: collect-data
  name: collect-data
spec:
  containers:
  - image: <namespace/image-name:tag>
    name: collect-data
    envFrom:
      - secretRef:
          name: scaleway-credentials
  dnsPolicy: ClusterFirst
  restartPolicy: Always
```

And apply the manifest to your Kubernetes cluster. *Be careful* you need to deploy a secret first with 3 values :

```
kubectl create secret generic \
--from-literal ACCESS_KEY=<your-access-key> \
--from-literal SECRET_KEY=<your-secret-key> \
--from-literal BUCKET=<your-s3-name> \
scaleway-credentials
```
