#!/bin/bash

# Script de ayuda para Docker - Plexus Asset Management

set -e

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Función para mostrar ayuda
show_help() {
    echo -e "${BLUE}Plexus Asset Management - Docker Helper${NC}"
    echo ""
    echo "Uso: $0 [COMANDO]"
    echo ""
    echo "Comandos disponibles:"
    echo "  build       - Construir la imagen Docker"
    echo "  run         - Ejecutar la aplicación con docker-compose"
    echo "  stop        - Detener todos los contenedores"
    echo "  restart     - Reiniciar todos los contenedores"
    echo "  logs        - Mostrar logs de la aplicación"
    echo "  logs-all    - Mostrar logs de todos los servicios"
    echo "  clean       - Limpiar contenedores e imágenes"
    echo "  status      - Mostrar estado de los contenedores"
    echo "  shell       - Abrir shell en el contenedor de la aplicación"
    echo "  test        - Ejecutar tests dentro del contenedor"
    echo "  help        - Mostrar esta ayuda"
    echo ""
    echo "Ejemplos:"
    echo "  $0 build && $0 run"
    echo "  $0 logs -f"
    echo "  $0 clean"
}

# Función para construir la imagen
build_image() {
    echo -e "${YELLOW}Construyendo imagen Docker...${NC}"
    docker build -t plexus-app:latest .
    echo -e "${GREEN}Imagen construida exitosamente${NC}"
}

# Función para ejecutar con docker-compose
run_app() {
    echo -e "${YELLOW}Iniciando aplicación con docker-compose...${NC}"
    docker-compose up -d
    echo -e "${GREEN}Aplicación iniciada${NC}"
    echo -e "${BLUE}Accede a:${NC}"
    echo "  - Aplicación: http://localhost:8080"
    echo "  - Swagger UI: http://localhost:8080/swagger-ui.html"
    echo "  - H2 Console: http://localhost:8082"
    echo "  - Health Check: http://localhost:8080/actuator/health"
}

# Función para detener contenedores
stop_app() {
    echo -e "${YELLOW}Deteniendo contenedores...${NC}"
    docker-compose down
    echo -e "${GREEN}Contenedores detenidos${NC}"
}

# Función para reiniciar
restart_app() {
    echo -e "${YELLOW}Reiniciando aplicación...${NC}"
    docker-compose restart
    echo -e "${GREEN}Aplicación reiniciada${NC}"
}

# Función para mostrar logs
show_logs() {
    docker-compose logs -f plexus-app
}

# Función para mostrar todos los logs
show_logs_all() {
    docker-compose logs -f
}

# Función para limpiar
clean_docker() {
    echo -e "${YELLOW}Limpiando contenedores e imágenes...${NC}"
    docker-compose down -v --remove-orphans
    docker system prune -f
    docker image prune -f
    echo -e "${GREEN}Limpieza completada${NC}"
}

# Función para mostrar estado
show_status() {
    echo -e "${BLUE}Estado de los contenedores:${NC}"
    docker-compose ps
}

# Función para abrir shell
open_shell() {
    echo -e "${YELLOW}Abriendo shell en el contenedor...${NC}"
    docker-compose exec plexus-app /bin/bash
}

# Función para ejecutar tests
run_tests() {
    echo -e "${YELLOW}Ejecutando tests...${NC}"
    docker-compose exec plexus-app mvn test
}

# Main script
case "${1:-help}" in
    build)
        build_image
        ;;
    run)
        run_app
        ;;
    stop)
        stop_app
        ;;
    restart)
        restart_app
        ;;
    logs)
        show_logs
        ;;
    logs-all)
        show_logs_all
        ;;
    clean)
        clean_docker
        ;;
    status)
        show_status
        ;;
    shell)
        open_shell
        ;;
    test)
        run_tests
        ;;
    help|--help|-h)
        show_help
        ;;
    *)
        echo -e "${RED}Comando desconocido: $1${NC}"
        show_help
        exit 1
        ;;
esac
