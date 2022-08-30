//aus den HTML-Elementen werden die Informationen ueber die verwendeten IDs genommen
const acceptCall= document.getElementById("accept-call");



acceptCall.addEventListener("click", (e) => {
   e.preventDefault();
   alert("Seite funktioniert");})

