
This ansible playbook is taken from [deploy-application-example](https://github.com/killerwhile/deploy-application-example). Thanks to B. Perroud for the help.

# SSH Config

In `~/.ssh/config`

```
Host pubgw1.daplab.ch
    Port 2202
    PreferredAuthentications publickey
    IdentityFile ~/.ssh/id_rsa
    ForwardAgent yes
    ProxyCommand none
    ControlPersist 60s
    ControlMaster auto
    ControlPath ~/.ssh/ssh_control_%h_%p_%r
#    User bperroud
```

# Deploying an application

```
ansible-playbook -i inventory.ini deploy-symbiosart.yml -s -e symbiosarttesting=1
```

* `symbiosarttesting=1` is used to download the RPM directly from [Jenkins](https://jenkins.daplab.ch), i.e. for
testing :)
* The version to deploy has to be set in [roles/symbiosart/defaults/main.yml](roles/symbiosart/defaults/main.yml)
