var GraphAPI = function(canvasElement, data, dateSpan) {
    var self = this;

    (function() {
        self.mouseX = 0,
        self.mouseY = 0,
        self.mousePressed = false,
        self.isTesting = false,
        self.monthMilliseconds = 2592000000,
        self.oneDay = 1 * 24 * 60 * 60 * 1000,
        self.oneMonth = self.oneDay*30,
        self.canvasElementName = '#' + canvasElement,
        self.eGraphDiv = document.getElementById(canvasElement),
        self.jGraphDiv = $(self.eGraphDiv),
        self.plot = null,
        self.ctx = null,
        self.gfx = null,
        self.med = null,
        self.choiceList = [],
        self.dataset = data,
        self.dateSpan = dateSpan,
        self.displayData = [],
        self.maxCheckedboxes = 5,
        self.checkedCounter = 0,
        self.currentSelectedData = null,
        self.synchronizedGraphAPI = null,
        self.options = {
                            points: {show: true, symbol: "square"},
                            lines: {show: true, fill: true},
                            xaxis: {
                                    mode: "time",
                                    timeformat: "%b %y",
                                    monthNames: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
                                    minTickSize: [1, "month"],
                                    min: dateSpan.start,
                                    max: dateSpan.end,
                                    color: "#fff"
                                    },
                            yaxis:  {
                                    color: "#fff"
                                    },
                            legend: {
                                    position: "nw",
                                    backgroundOpacity: .8,
                                    backgroundColor: "#fff",
                                    show: true
                                    },
                            grid: {show: true, backgroundColor: {colors: ["#ccc", "#fff"]}, borderColor: "#77f", clickable: true, hoverable: true}
                        };
        setupSelection(data);
        setupTooltip();
        self.displayData = getSelectedData();
        plotFunc();
    })(jQuery);
    
    self.draw = function() {
        plotFunc();
    }

    return self;
    function setupSelection(data) {
        if(data==null) {return;}
        for(var i in data) {
            var choiceName = i;
            self.choiceList.push(choiceName);
            var hash = data[i].data;
            if(hash==null) {return;}
            var choice = document.getElementById(choiceName);
            var jChoice = $(choice);

            $.each(hash, function(key, val) {
                var id = 'id' + choiceName + val.label;
                id = removeSpecial(id);
                jChoice.append('<input type="checkbox" name="' + val.label +
                                   '" id="' + id + '">' +
                                   '<label for="id' + val.label + '">'
                                    + val.label + '</label><br>');
                $('#' + id).data(id, val);
                $('#' + id).click(function() {
                    self.displayData = getSelectedData();
                    plotFunc();
                });
                if(self.checkedCounter < self.maxCheckedboxes) {
                    self.checkedCounter++;
                    $('#' + id).attr('checked', true);
                }
            });
        }
    }
    function setupTooltip() {
        var previousPoint = null;
        self.jGraphDiv.bind("plothover", function (event, pos, item) {
            if (item) {
                if (previousPoint != item.datapoint) {
                    previousPoint = item.datapoint;
                    $("#tooltip").remove();

                    var label = item.series.label,
                        x = item.datapoint[0],
                        y = item.datapoint[1],
                        d = new Date(x);
                    showTooltip(item.pageX, item.pageY-40, label + ": "+ y + " on " + d.format("mmmm dS, yyyy"));
                }
            }
            else {
                $("#tooltip").remove();
                previousPoint = null;
            }
        });
    }
    function showTooltip(x, y, contents) {
        $('<div id="tooltip">' + contents + '</div>').css( {
            position: 'absolute',
            display: 'none',
            top: y + 5,
            left: x + 5,
            border: '1px solid #fdd',
            padding: '2px',
            color: '#000',
            'background-color': '#ffb',
            opacity: 0.80
        }).appendTo("body").show();
    }
    function removeSpecial(str) {
        var temp = '',
            length = str.length;
        for(var i = 0; i < length; i++) {
            if( (str.charAt(i)!=' ') && (str.charAt(i)!='/') ) {
                temp += str.charAt(i);
            }
        }
        return temp;
    }
    function getSelectedData() {
        var data = [];
        for(var i in self.choiceList) {
            var choice = document.getElementById(self.choiceList[i]);
            var jChoice = $(choice);
            var found = jChoice.find(':checked');
            found.each( function() {
                data.push($(this).data($(this).attr('id')));
            });
        }
        return data;
    }
    function plotFunc() {
        self.plot = $.plot(self.jGraphDiv, self.displayData, self.options);
    }
    function dumpProps(obj, parent) {
       for (var i in obj) {
          if (parent) {var msg = parent + "." + i + "\n" + obj[i];} else {var msg = i + "\n" + obj[i];}
          if (!confirm(msg)) {return;}
          if (typeof obj[i] == "object") {
             if (parent) {dumpProps(obj[i], parent + "." + i);} else {dumpProps(obj[i], i);}
          }
       }
    }
};