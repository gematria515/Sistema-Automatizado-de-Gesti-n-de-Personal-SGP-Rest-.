// Carga los datos del empleado en el modal Agregar o Editar
document.addEventListener('DOMContentLoaded', function () {
    const modal = document.getElementById('empleadoModal');
    if (!modal) {
        return;
    }
    const form = document.getElementById('empleadoForm');
    const header = document.getElementById('empleadoModalHeader');
    const titulo = document.getElementById('empleadoModalTitulo');
    const id = document.getElementById('empleadoId');
    const nombre = document.getElementById('empleadoNombre');
    const correo = document.getElementById('empleadoCorreo');
    const puesto = document.getElementById('empleadoPuesto');
    const area = document.getElementById('empleadoArea');
    const tarifaHora = document.getElementById('empleadoTarifaHora');
    const activo = document.getElementById('empleadoActivo');
    modal.addEventListener('show.bs.modal', function (event) {
        const button = event.relatedTarget;
        if (!button) {
            return;
        }
        const modo = button.getAttribute('data-modo');
        // Se limpia cualquier información de una apertura anterior
        form.reset();
        id.value = '';
        titulo.textContent =button.getAttribute('data-titulo') || '';
        header.classList.remove('bg-info','bg-warning','text-white');
        if (modo === 'editar') {
            id.value =button.getAttribute('data-id') || '';
            nombre.value =button.getAttribute('data-nombre') || '';
            correo.value =button.getAttribute('data-correo') || '';
            puesto.value =button.getAttribute('data-puesto') || '';
            area.value =button.getAttribute('data-area') || '';
            tarifaHora.value =button.getAttribute('data-tarifahora') || '';
            activo.checked =button.getAttribute('data-activo') === 'true';
            header.classList.add('bg-warning');
        } else {
            // Un registro nuevo inicia activo
            activo.checked = true;
            header.classList.add('bg-info','text-white');
        }
    });
});
