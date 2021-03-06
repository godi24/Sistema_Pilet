$(document).ready(function() {
    $.fn.initBootTable = function() {
        $(this).bootstrapTable('destroy');
        $(this).bootstrapTable().
        unbind('check.bs.table').on('check.bs.table', function (e, row) {
            consObjeSali([{name : 'codiObjePara', value : row.id.trim()}]);
            var cadena =row.movimiento.trim();
            if(cadena=='<span class="label label-danger"><i class="fa fa-lock"></i> Salida</span>')
            {

                $('#SaliForm').modal('show');

            }
            else
                {
                 $('#ModaForm').modal('show');
            }

            var res = cadena.substring(10, 4);
            console.log(row.movimiento.trim());

        });
        return false;
    };
    $.fn.initDatePick = function() {
        $(this).datepicker({
            format: "dd/mm/yyyy",
            language: "es",
            orientation: "top auto",
            autoclose: true,
            todayHighlight: true
        }).on('show.bs.modal', function(e) {
            // Quitar el conflicto con bootstrap modal "show.bs.modal"
            e.stopPropagation();
        }).on('hide.bs.modal', function(e) {
            // Quitar el conflicto con bootstrap modal "hide.bs.modal"
            e.stopPropagation();
        });
    };
    $('#SaliForm').on('show.bs.modal', function() {
        INIT_OBJE_MODA_SALI();
    });
    $('#SaliForm').on('hide.bs.modal', function() {
        $("#TablRegi").bootstrapTable('uncheckAll');
    });

    INIT_OBJE_SALI();
    INIT_OBJE();
});

function INIT_OBJE_SALI()
{
    $("#TablRegi").initBootTable();
    INIT_OBJE_MODA_SALI();
}

function INIT_OBJE_MODA_SALI()
{
    $("#FormRegi1\\:btonElim").confirmation({container: '#FormRegi1'});
    $("#FormRegi1\\:fech1").initDatePick();
}


function INIT_OBJE()
{
    $("#TablRegi").initBootTable();
    INIT_OBJE_MODA();
}

function INIT_OBJE_MODA()
{
    $("#FormRegi\\:btonElim").confirmation({container: '#FormRegi'});
     $("#FormRegi\\:fech").initDatePick();
}
