Vagrant.configure("2") do |config|
  config.vm.box = "ubuntu/bionic64"
  config.vm.provider "virtualbox" do |v|
      v.memory = 2048
      v.cpus = 2
  end
  config.vm.provision "ansible" do |ansible|
    ansible.galaxy_role_file = "Vagrantfile-ansible-requirements.yml"
    ansible.galaxy_roles_path = ".ansible-galaxy-roles"
    ansible.playbook = "Vagrantfile-playbook.yml"
    ansible.extra_vars = {
      ansible_python_interpreter: "/usr/bin/python3",
    }
  end
end
