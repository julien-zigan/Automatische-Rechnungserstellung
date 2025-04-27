// SumTotal logic 
updateSumTotal();

const volatiles = [
  "duration",
  "rate",
  "isPaid",
  "notPaid",
  "travelFee",
];

volatiles.forEach(registerListenersSumTotal);

function registerListenersSumTotal(id) {
  document
    .getElementById(id)
    .addEventListener("change", updateSumTotal);
}

function updateSumTotal() {
  const display = document.getElementById("display");
  let duration = document.getElementById("duration").value;
  let rate = document.getElementById("rate").value;
  let travelPaid = document.getElementById("isPaid").checked;
  let travelFee = eval(
    travelPaid ? document.getElementById("travelFee").value : 0
  );

  display.innerHTML = duration * rate + travelFee + " €";
}



// Preview button logic
document.getElementById("preview").style.display = "none";

for (let i = 0; i < 9; i++) {
  registerListeners(i.toString());
}

function registerListeners(id) {
  document.getElementById(id).addEventListener("change", updateSubmit);
}

function updateSubmit() {
  let show = true;
  for (let i = 0; i < 9; i++) {
    if (!document.getElementById(i).checked) {
      show = false;
      break;
    }
  }
  document.getElementById("preview").style.display = show ? "block" : "none";
}
