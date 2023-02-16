function getAllClients(){
    let tBody = document.getElementById("tBody");
    tBody.innerHTML = "";

    fetch('http://localhost:8080/api/clients')
        .then(response => response.json())
        .then(clients => {
            clients.forEach(function (client) {
                var row = tBody.insertRow();
                var cell0 = row.insertCell();
                cell0.innerHTML = client.id;
                var cell1 = row.insertCell();
                cell1.innerHTML = client.name;

                var cell3 = row.insertCell();
                cell3.innerHTML = client.address.street;

                var phoneList = document.createElement('ul');
                client.phones.forEach(function (phone) {
                    let li = document.createElement('li');
                    li.textContent = phone.number + " ";
                    phoneList.appendChild(li);
                });
                var cell4 = row.insertCell();
                cell4.innerHTML = phoneList.textContent;
            })
        });
}
