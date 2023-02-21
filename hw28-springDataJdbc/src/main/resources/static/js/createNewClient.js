function createNewClient() {

    fetch('http://localhost:8080/api/client', {
        method: 'POST',
        body: JSON.stringify({
            name: window.newClientForm.new_name.value,
            address: { street: window.newClientForm.new_address.value },
            phones: [   { number: window.newClientForm.new_phone_home.value},
                        { number: window.newClientForm.new_phone_mobile.value}
            ]
        }),
        headers: {"Content-type": "application/json; charset=UTF-8"}
    })
        .then(response => {
            getAllClients();
        });
}