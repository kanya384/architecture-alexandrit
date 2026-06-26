# Jaeger в Minikube с сервисами

## Описание
Развертывание Jaeger в Minikube с двумя сервисами, которые:
1. Взаимодействуют между собой
2. Отправляют трейсы в Jaeger

## Требования
- Minikube
- kubectl
- Docker

## Установка

### 1. Запуск Minikube 
```bash
minikube start --addons=ingress 
```
Ingress нужен для вызовов

### 2. Установка cert-manager
```bash
kubectl apply -f https://github.com/cert-manager/cert-manager/releases/download/v1.13.3/cert-manager.yaml
```

### 3. Развертывание Jaeger
```bash
kubectl create namespace observability
kubectl create -f https://github.com/jaegertracing/jaeger-operator/releases/download/v1.51.0/jaeger-operator.yaml -n observability
kubectl set image deployment/jaeger-operator kube-rbac-proxy=quay.io/brancz/kube-rbac-proxy:v0.13.1 -n observability
kubectl apply -f k8s/jaeger-instance.yaml
```

### 4. Сборка и деплой сервисов
```bash
# Сборка образов
minikube image build -t calculator:latest services/calculator/
minikube image build -t orders:latest services/orders/

# Развертывание
kubectl apply -f k8s/services.yaml
```

## Проверка работы

### Доступ к Jaeger UI
```bash
kubectl port-forward svc/simplest-query 16686:16686
```
Откройте в браузере: http://localhost:16686

### Тестирование сервисов
```bash
# Вызов service-a, который вызывает service-b
kubectl exec -it $(kubectl get pods -l app=orders -o jsonpath='{.items[0].metadata.name}') -- curl -s http://orders:8080/order
kubectl exec -it $(kubectl get pods -l app=calculator -o jsonpath='{.items[0].metadata.name}') -- curl -s http://calculator:8090/calculate
```

## Структура проекта
- `services/service-a/` - Исходный код service-a
- `services/service-b/` - Исходный код service-b  
- `k8s/services.yaml` - Конфигурация Kubernetes для сервисов
- `jaeger-instance.yaml` - Конфигурация Jaeger