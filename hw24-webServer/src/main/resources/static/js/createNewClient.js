function createNewClient() {

    fetch('http://localhost:8080/api/client', {
        method: 'POST',
        body: JSON.stringify({
            name: window.newClientForm.new_name.value,
            login: window.newClientForm.new_login.value,
            password: window.newClientForm.new_password.value,
            address: { street: window.newClientForm.new_address.value },
            phones: [ { number: window.newClientForm.new_phone.value} ]
        }),
        headers: {"Content-type": "application/json; charset=UTF-8"}
    })
        .then(response => {
            window.newClientForm.new_name.value = "";
            window.newClientForm.new_login.value = "";
            window.newClientForm.new_password.value = "";
            window.newClientForm.new_address.value = "";
            window.newClientForm.new_phone.value = "";
        });
}