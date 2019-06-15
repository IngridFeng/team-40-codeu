google.charts.load('current', {packages: ['corechart']});
google.charts.setOnLoadCallback(drawSentimentStats);

function drawSentimentStats(nice_data){
  // process text + sentiment info of all messages
    fetch("/feed")
      .then((response) => {
        return response.json();
      })
      .then((messageJson) => {
        var nice_data = new google.visualization.DataTable();
        nice_data.addColumn('string', 'Text');
        nice_data.addColumn('number', 'Constructiveness');

        var sum = 0
        var count = 0

        for (i=0; i < messageJson.length; i++){
          niceRow = [];
          var curText = messageJson[i].text;
          var curConstructiveness = messageJson[i].sentiment;
          niceRow.push(curText, curConstructiveness);
          nice_data.addRow(niceRow);

          sum += messageJson[i].sentiment
          count ++
        }

        // draw chart
        var nice_chart = new google.visualization.Histogram(document.getElementById('nice_chart'));
        nice_chart.draw(nice_data);

        // show average sentiment
        const avg_sentiment = sum/count
        var div = document.getElementById('avg_sentiment')

        switch (avg_sentiment) {
          case (avg_sentiment < 0):
            div.innerText = 'Positive reinforcement is the only thing that builds up!';
            break;
          case (avg_sentiment > 0.5):
            div.innerText = 'Critique makes us better!';
            break;
          default:
            div.innerText = 'We\'re doing Amazing! Thanks for helping everybody!';
        }
    });
}
