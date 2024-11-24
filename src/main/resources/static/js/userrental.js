function showMiniCalendar(year, month) {
    var today = new Date();
    var thisYear = year || today.getFullYear();
    var thisMonth = month || today.getMonth() + 1;
    var days = new Date(thisYear, thisMonth, 0).getDate();
    var firstDay = new Date(thisYear, thisMonth - 1, 1).getDay();
    var lastDay = new Date(thisYear, thisMonth - 1, days).getDay();

    var miniCalendar = document.getElementById('mini-calendar');
    miniCalendar.innerHTML = '';
    var header = document.createElement('h2');
    header.textContent = `${thisYear}년 ${thisMonth}월`;
    miniCalendar.appendChild(header);

    var dayNames = ['일', '월', '화', '수', '목', '금', '토'];
    var weekdays = document.createElement('div');
    weekdays.className = 'mini-calendar';
    for (var i = 0; i < dayNames.length; i++) {
      var dayName = document.createElement('div');
      dayName.textContent = dayNames[i];
      weekdays.appendChild(dayName);
    }
    miniCalendar.appendChild(weekdays);

    var date = 1;
    var rows = Math.ceil((days + firstDay) / 7);
    for (var i = 0; i < rows; i++) {
      var row = document.createElement('div');
      row.className = 'mini-calendar';
      for (var j = 0; j < 7; j++) {
        var day = document.createElement('div');
        if (i === 0 && j < firstDay) {
          day.textContent = '';
        } else if (date > days) {
          day.textContent = '';
        } else {
          day.textContent = date;
          if (thisYear === today.getFullYear() &&
              thisMonth === today.getMonth() + 1 &&
              date === today.getDate()) {
            day.style.backgroundColor = 'yellow';
          }
          date++;
        }
        row.appendChild(day);
      }
      miniCalendar.appendChild(row);
    }
  }

  showMiniCalendar();