// Carga los datos del turno en el modal Agregar o Editar
document.addEventListener('DOMContentLoaded', function () {
    const modal = document.getElementById('turnoModal');
    if (!modal) {
        return;
    }
    const form = document.getElementById('turnoForm');
    const header = document.getElementById('turnoModalHeader');
    const titulo = document.getElementById('turnoModalTitulo');
    const id = document.getElementById('turnoId');
    const empleado = document.getElementById('turnoEmpleado');
    const fecha = document.getElementById('turnoFecha');
    const horaInicio = document.getElementById('turnoHoraInicio');
    const horaFin = document.getElementById('turnoHoraFin');
    const area = document.getElementById('turnoArea');
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
            empleado.value =button.getAttribute('data-empleado') || '';
            fecha.value =button.getAttribute('data-fecha') || '';
            horaInicio.value =button.getAttribute('data-horainicio') || '';
            horaFin.value =button.getAttribute('data-horafin') || '';
            area.value =button.getAttribute('data-area') || '';
            header.classList.add('bg-warning');
        } else {
            header.classList.add('bg-info','text-white');
        }
    });
});
