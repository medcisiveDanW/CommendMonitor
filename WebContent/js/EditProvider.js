var EditProvider = function(targetId) {
    var self = this;

    (function() {
        self.targetId = targetId,
        self.addCosigner = new AddCosigner(self),
        self.u = new Utility();
        _init();
    })(jQuery);

    self.reset = function() {
        _init();
    }

    return self;

    function _init() {
        var block =
        '<div class="ui-widget">'
        + '     <div style="margin-top: 20px; padding: 0 .7em;" class="ui-state-highlight ui-corner-all">'
        + '         <p><span style="float: left; margin-right: .3em; margin-top: 3px;" class="ui-icon ui-icon-info"></span>'
        + '         <strong>STEP 1: Filter by Providers Name or Sta3n.</strong>'
        + '             <input id="editProviderNameInputId" type="text" class="addClinicInputClass" value="Providers Name"/>or'
        + '             <input id="editProviderSta3nInputId" type="text" class="addClinicInputClass" value="Providers Sta3n"/>'
        + '         </p>'
        + '     </div>'
        + '</div>';
        $('#' + self.targetId).html(block);
        $('#editProviderNameInputId').click(function() {$('#editProviderNameInputId').val('');});
        $('#editProviderNameInputId').blur(function() {
            var val = $('#editProviderNameInputId').val();
            if(val=='') {
                $('#editProviderNameInputId').val('Providers Name');
            } else {}
        });
        $('#editProviderSta3nInputId').click(function() {$('#editProviderSta3nInputId').val('');});
        $('#editProviderSta3nInputId').blur(function() {
            var val = $('#editProviderSta3nInputId').val();
            if(val=='') {
                $('#editProviderSta3nInputId').val('Providers Sta3n');
            } else {}
        });
        $('.addClinicInputClass').keyup(function() {
            var providerName = $('#editProviderNameInputId').val(),
                sta3n = $('#editProviderSta3nInputId').val();
            if(providerName=='Providers Name') { providerName = ''; }
            if(sta3n=='Providers Sta3n') { sta3n = ''; }
            if(providerName.length>4 || sta3n.length>2) {
                _editProviderTable(providerName,sta3n);
            }
        });
    }

    function _editProviderTable(providerName,sta3n) {
        $('#editProviderTableWidgetId').remove();
        var data = null;
        $.ajax({
            type:   "post",
            url:    "ManagmentServlet",
            data:   "option=getInternalProviders&ProviderName=" + providerName + "&Sta3n=" + sta3n,
            async:  false,
            success: function(msg) {
                data = msg._table;
            },
            error: function(xhr, textStatus, errorThrown) {
                self.u.print(xhr);
                self.u.print(textStatus);
                self.u.print(errorThrown);
            }
        });
        var module =
            '<table id="editProviderTableId" cellpadding="0" cellspacing="0" border="0" class="display" width="100%">' +
                '<thead>' +
                    '<tr>' +
                        '<th>Sta3n</th>' +
                        '<th>Provider IEN</th>' +
                        '<th>Provider SID</th>' +
                        '<th>Provider Name</th>' +
                        '<th>Cosigner Name</th>' +
                        '<th>Edit Cosigner</th>' +
                        '<th>Edit Clinics</th>' +
                    '</tr>' +
                '</thead>' +
                '<tbody>';
            for(i in data) {
                var cur = data[i],
                    cosign = '';
                if(cur.cosignerName!=undefined) {
                    cosign = cur.cosignerName;
                }
                module +=
                    '<tr class="gradeC">' +
                        '<td style="text-align: center;">' + cur.sta3n + '</td>' +
                        '<td style="text-align: center;">' + cur.DUZ + '</td>' +
                        '<td style="text-align: center;">' + cur.SID + '</td>' +
                        '<td style="text-align: center;">' + cur.name + '</td>' +
                        '<td style="text-align: center;">' + cosign + '</td>' +
                        '<td class="center"><img id="editProviderCosignerSelectionImgId' + i + '" class="icon-class" src="images/edit.png" alt="Edit Cosigner"/></td>' +
                        '<td class="center"><img id="editProviderSelectionImgId' + i + '" class="icon-class" src="images/edit.png" alt="Edit Clinics"/></td>' +
                    '</tr>';
            }
        module +=
                '</tbody>' +
            '</table>';

        var block =
        '<div id="editProviderTableWidgetId" class="ui-widget">'
        + '    <div style="margin-top: 20px; padding: 0 .7em;" class="ui-state-highlight ui-corner-all">'
        + '        <p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>'
        + '        <strong>STEP 2: Select Provider to edit.</strong>'
        + module
        + '        </p>'
        + '    </div>'
        + '</div>';
        $('#' + self.targetId).append(block);
        $('#editProviderTableId').dataTable({
            "bLengthChange": false
            ,"bJQueryUI": true
            ,"sScrollY": "300px"
            ,"bPaginate": false
            ,"sPaginationType": "full_numbers"
        });
        for(i in data) {
            $('#editProviderSelectionImgId' + i).click(function() {
                var d = data[this.id.replace("editProviderSelectionImgId","")];
                _initEditProvider(d);
            });
            $('#editProviderCosignerSelectionImgId' + i).click(function() {
                var d = data[this.id.replace("editProviderCosignerSelectionImgId","")];
                self.u.print(d);
                self.addCosigner.init(d);
            });
        }
    }

    function _initEditProvider(obj) {
        var block =
        '<div class="ui-widget">'
        + '     <div style="margin-top: 20px; padding: 0 .7em;" class="ui-state-highlight ui-corner-all">'
        + '         <p><span style="float: left; margin-right: .3em; margin-top: 7px;" class="ui-icon ui-icon-info"></span>'
        + '         <strong>STEP 3: Provider ' + obj.name + '</strong>'
        + '             <input id="removeProviderDeleteButtonId" class="buttonClass" type="button" value="Delete Provider"/>'
        + '             <input id="removeProviderClearButtonId" class="buttonClass" type="button" value="Clear Selected Provider"/>'
        + '         </p>'
        + '     </div>'
        + '</div>';
        $('#' + self.targetId).html(block);
        $('#removeProviderClearButtonId').click(function() {
            _init();
        });
        $('.buttonClass').button();
        $('#removeProviderDeleteButtonId').click(function() {
            $.ajax({
                type:   "post",
                url:    "ManagmentServlet",
                data:   "option=removeProvider&Sta3n=" + obj.sta3n + "&ProviderSID=" + obj.SID,
                async:  false,
                success: function(msg) {
                },
                error: function(xhr, textStatus, errorThrown) {
                    //window.location = 'login.html?error=' + errorThrown;
                    self.u.print(xhr);
                    self.u.print(textStatus);
                    self.u.print(errorThrown);
                }
            });
            _init();
        });
        _removeClinicTable(obj);
    }

    function _removeClinicTable(obj) {
        $('#removeClinicTableWidgetId').remove();
        var data = null;
        $.ajax({
            type:   "post",
            url:    "ManagmentServlet",
            data:   "option=getInternalClinics&Sta3n=" + obj.sta3n + "&ProviderSID=" + obj.SID,
            async:  false,
            success: function(msg) {
                data = msg._table;
            },
            error: function(xhr, textStatus, errorThrown) {
                self.u.print(xhr);
                self.u.print(textStatus);
                self.u.print(errorThrown);
            }
        });
        var module =
            '<table id="editClinicProviderTableId" cellpadding="0" cellspacing="0" border="0" class="display" width="100%">' +
                '<thead>' +
                    '<tr>' +
                        '<th>Sta3n</th>' +
                        '<th>LocationIEN</th>' +
                        '<th>LocationSID</th>' +
                        '<th>LocationName</th>' +
                        '<th>Remove</th>' +
                    '</tr>' +
                '</thead>' +
                '<tbody>';
            for(i in data) {
                var cur = data[i];
                module +=
                    '<tr class="gradeC">' +
                        '<td style="text-align: center;">' + cur.sta3n + '</td>' +
                        '<td style="text-align: center;">' + cur.TrtLocIEN + '</td>' +
                        '<td style="text-align: center;">' + cur.LocationSID + '</td>' +
                        '<td style="text-align: center;">' + cur.clinicLocName + '</td>' +
                        '<td class=""><div style="margin: 0 auto;width: 25px;"><img id="removeClinicSelectionImgId' + i + '" class="icon-class" src="images/X-icon-small.png" alt="Remove"/></div></td>' +
                    '</tr>';
            }
        module +=
                '</tbody>' +
            '</table>';

        var block =
        '<div id="removeClinicTableWidgetId" class="ui-widget">'
        + '    <div style="margin-top: 20px; padding: 0 .7em;" class="ui-state-highlight ui-corner-all">'
        + '        <p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>'
        + '        <strong>STEP 4: Select Clinics to remove.</strong>'
        + module
        + '        </p>'
        + '    </div>'
        + '</div>';
        $('#' + self.targetId).append(block);
        $('#editClinicProviderTableId').dataTable({
            "bLengthChange": false
            ,"bJQueryUI": true
            ,"sScrollY": "300px"
            ,"bPaginate": false
            ,"sPaginationType": "full_numbers"
        });
        for(i in data) {
            $('#removeClinicSelectionImgId' + i).click(function() {
                var d = data[this.id.replace("removeClinicSelectionImgId","")];
                $.ajax({
                    type:   "post",
                    url:    "ManagmentServlet",
                    data:   "option=removeClinic&Sta3n=" + obj.sta3n + "&ProviderSID=" + obj.SID + "&LocationSID=" + d.LocationSID,
                    async:  false,
                    success: function(msg) {
                        _removeClinicTable(obj);
                    },
                    error: function(xhr, textStatus, errorThrown) {
                        //window.location = 'login.html?error=' + errorThrown;
                        self.u.print(xhr);
                        self.u.print(textStatus);
                        self.u.print(errorThrown);
                    }
                });
            });
        }
    }
};
