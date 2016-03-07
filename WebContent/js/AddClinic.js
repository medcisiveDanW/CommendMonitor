var AddClinic = function(targetId) {
    var self = this;

    (function() {
        self.targetId = targetId,
        self.u = new Utility();
        _init();
    })(jQuery);

    self.addClinicsToProvider = function(obj) {
        var result = _initAddClinicToProvider(obj);
        if(result==false) { _init(); }
    }

    return self;

    function _init() {
        var block =
        '<div class="ui-widget">'
        + '     <div style="margin-top: 20px; padding: 0 .7em;" class="ui-state-highlight ui-corner-all">'
        + '         <p><span style="float: left; margin-right: .3em; margin-top: 3px;" class="ui-icon ui-icon-info"></span>'
        + '         <strong>STEP 1: Filter by Providers Name or Sta3n.</strong>'
        + '             <input id="addClinicProviderNameInputId" type="text" class="addClinicInputClass" value="Providers Name"/>or'
        + '             <input id="addClinicProviderSta3nInputId" type="text" class="addClinicInputClass" value="Providers Sta3n"/>'
        + '         </p>'
        + '     </div>'
        + '</div>';
        $('#' + self.targetId).html(block);
        $('#addClinicProviderNameInputId').click(function() {$('#addClinicProviderNameInputId').val('');});
        $('#addClinicProviderNameInputId').blur(function() {
            var val = $('#addClinicProviderNameInputId').val();
            if(val=='') {
                $('#addClinicProviderNameInputId').val('Providers Name');
            } else {}
        });
        $('#addClinicProviderSta3nInputId').click(function() {$('#addClinicProviderSta3nInputId').val('');});
        $('#addClinicProviderSta3nInputId').blur(function() {
            var val = $('#addClinicProviderSta3nInputId').val();
            if(val=='') {
                $('#addClinicProviderSta3nInputId').val('Providers Sta3n');
            } else {}
        });
        $('.addClinicInputClass').keyup(function() {
            var providerName = $('#addClinicProviderNameInputId').val(),
                sta3n = $('#addClinicProviderSta3nInputId').val();
            if(providerName=='Providers Name') { providerName = ''; }
            if(sta3n=='Providers Sta3n') { sta3n = ''; }
            if(providerName.length>3 || sta3n.length>2) {
                _addProviderTable(providerName,sta3n);
            }
        });
    }

    function _addProviderTable(providerName,sta3n) {
        $('#addClinicProviderTableWidgetId').remove();
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
            '<table id="addClinicProviderTableId" cellpadding="0" cellspacing="0" border="0" class="display" width="100%">' +
                '<thead>' +
                    '<tr>' +
                        '<th>Sta3n</th>' +
                        '<th>ProviderIEN</th>' +
                        '<th>ProviderSID</th>' +
                        '<th>ProviderName</th>' +
                        '<th>Select</th>' +
                    '</tr>' +
                '</thead>' +
                '<tbody>';
            for(i in data) {
                var cur = data[i];
                module +=
                    '<tr class="gradeC">' +
                        '<td>' + cur.sta3n + '</td>' +
                        '<td>' + cur.DUZ + '</td>' +
                        '<td>' + cur.SID + '</td>' +
                        '<td>' + cur.name + '</td>' +
                        '<td class="center"><img id="addProviderSelectionImgId' + i + '" class="icon-class" src="images/add.png" alt="Select"/></td>' +
                    '</tr>';
            }
        module +=
                '</tbody>' +
            '</table>';

        var block =
        '<div id="addClinicProviderTableWidgetId" class="ui-widget">'
        + '    <div style="margin-top: 20px; padding: 0 .7em;" class="ui-state-highlight ui-corner-all">'
        + '        <p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>'
        + '        <strong>STEP 2: Select Provider to add.</strong>'
        + module
        + '        </p>'
        + '    </div>'
        + '</div>';
        $('#' + self.targetId).append(block);
        $('#addClinicProviderTableId').dataTable({
            "bLengthChange": false
            ,"bJQueryUI": true
            ,"sScrollY": "300px"
            ,"bPaginate": false
            ,"sPaginationType": "full_numbers"
        });
        for(i in data) {
            $('#addProviderSelectionImgId' + i).click(function() {
                var d = data[this.id.replace("addProviderSelectionImgId","")];
                _initAddClinicToProvider(d);
            });
        }
    }

    function _initAddClinicToProvider(obj) {
        var block =
        '<div class="ui-widget">'
        + '     <div style="margin-top: 20px; padding: 0 .7em;" class="ui-state-highlight ui-corner-all">'
        + '         <p><span style="float: left; margin-right: .3em; margin-top: 7px;" class="ui-icon ui-icon-info"></span>'
        + '         <strong>STEP 3: Add Clinics for provider ' + obj.name + '. </strong>'
        + '             <input id="addClinicClinicNameInputId" type="text" value="Clinic Name"/>'
        + '             <input id="addClinicClearButtonId" class="buttonClass" type="button" value="Clear Selected Provider"/>'
        + '         </p>'
        + '     </div>'
        + '</div>';
        $('#' + self.targetId).html(block);
        $('#addClinicClinicNameInputId').click(function() {$('#addClinicClinicNameInputId').val('');});
        $('#addClinicClinicNameInputId').blur(function() {
            var val = $('#addClinicClinicNameInputId').val();
            if(val=='') {
                $('#addClinicClinicNameInputId').val('Clinic Name');
            } else {}
        });
        $('#addClinicClinicNameInputId').keyup(function() {
            var clinicName = $('#addClinicClinicNameInputId').val();
            if(clinicName=='Clinic Name') { clinicName = ''; }
            if(clinicName.length>3) {
                _addClinicTable(clinicName,obj);
            }
        });
        $('#addClinicClearButtonId').click(function() {
            _init();
        });
        $('.buttonClass').button();
    }

    function _addClinicTable(clinicName,obj) {
        $('#addClinicProviderTableWidgetId').remove();
        var clinics = null;
        $.ajax({
            type:   "post",
            url:    "ManagmentServlet",
            data:   "option=getExternalClinics&ClinicName=" + clinicName + "&Sta3n=" + obj.sta3n,
            async:  false,
            success: function(msg) {
                clinics = msg._table;
            },
            error: function(xhr, textStatus, errorThrown) {
                self.u.print(xhr);
                self.u.print(textStatus);
                self.u.print(errorThrown);
            }
        });
        var module =
            '<table id="addClinicProviderTableId" cellpadding="0" cellspacing="0" border="0" class="display" width="100%">' +
                '<thead>' +
                    '<tr>' +
                        '<th>Sta3n</th>' +
                        '<th>LocationIEN</th>' +
                        '<th>LocationSID</th>' +
                        '<th>LocationName</th>' +
                        '<th>Select</th>' +
                    '</tr>' +
                '</thead>' +
                '<tbody>';
            for(i in clinics) {
                var cur = clinics[i];
                module +=
                    '<tr class="gradeC">' +
                        '<td>' + cur.Sta3n + '</td>' +
                        '<td>' + cur.LocationIEN + '</td>' +
                        '<td>' + cur.LocationSID + '</td>' +
                        '<td>' + cur.LocationName + '</td>' +
                        '<td class="center"><img id="addClinicSelectionImgId' + i + '" class="icon-class" src="images/add.png" alt="Select"/></td>' +
                    '</tr>';
            }
        module +=
                '</tbody>' +
            '</table>';

        var block =
        '<div id="addClinicProviderTableWidgetId" class="ui-widget">'
        + '    <div style="margin-top: 20px; padding: 0 .7em;" class="ui-state-highlight ui-corner-all">'
        + '        <p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>'
        + '        <strong>STEP 4: Select Clinics to add.</strong>'
        + module
        + '        <span style="color: red;">Currently Added Clinics:</span>'
        + '        <span id="listOfAddedClinicsSpanId" style="color: red;"></span>'
        + '        </p>'
        + '    </div>'
        + '</div>';
        $('#' + self.targetId).append(block);
        $('#addClinicProviderTableId').dataTable({
            "bLengthChange": false
            ,"bJQueryUI": true
            ,"sScrollY": "300px"
            ,"bPaginate": false
            ,"sPaginationType": "full_numbers"
        });
        var locationList = [];
        for(i in clinics) {
            $('#addClinicSelectionImgId' + i).click(function() {
                var d = clinics[this.id.replace("addClinicSelectionImgId","")];
                if($.inArray(d.LocationName,locationList)==-1) {
                    locationList.push(d.LocationName);
                }
                $('#listOfAddedClinicsSpanId').html('');
                for(var j in locationList) {
                    $('#listOfAddedClinicsSpanId').append(locationList[j] + " ");
                }
                $.ajax({
                    type:   "post",
                    url:    "ManagmentServlet",
                    data:   "option=addClinic&Sta3n=" + obj.sta3n + "&ProviderIEN=" + obj.DUZ + "&ProviderSID=" + obj.SID + "&LocationIEN=" + d.LocationIEN + "&LocationSID=" + d.LocationSID + "&LocationName=" + d.LocationName,
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
            });
        }
    }
};