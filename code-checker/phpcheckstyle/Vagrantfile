# -*- mode: ruby -*-
# vi: set ft=ruby :


# Vagrantfile API/syntax version.
VAGRANTFILE_API_VERSION = "2"

Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|

  config.vm.box = "debian/jessie64"
  config.vm.box_version = "8.6.1"
 
   #Dev config 
   config.vm.define "phpcheckstyle_dev" do |phpcheckstyle_dev|
   end
   config.vm.provider "virtualbox" do |v|
      v.memory = 3072
      v.cpus = 4
      v.name = "phpcheckstyle_dev"
   end

  config.vm.network "private_network", ip: "192.168.50.66"
  
  
  config.vm.synced_folder ".", "/vagrant", type: "virtualbox"
     
  #
  # Middleware installation
  # The following provisions are executed as "root" 
  #
  config.vm.provision "install_composer", privileged: true, type: "shell", inline: "/vagrant/vagrant_config/scripts/install_composer.sh"
    
  #
  # Middleware installation
  # The following provisions are executed as "vagrant" 
  #
  config.vm.provision "install_composer_libraries", privileged: false, type: "shell", inline: "/vagrant/vagrant_config/scripts/install_composer_libraries.sh"
    
  #
  # Documentation & Code quality & Developers provisions
  # The following provisions are executed as "vagrant" and are only run when called explicitly 
  #
  
  if ARGV.include? '--provision-with'
  	config.vm.provision "run_phpunit", privileged: false, type: "shell", inline: "/vagrant/vendor/bin/phpunit"
  end
  
  if ARGV.include? '--provision-with'
    config.vm.provision "run_phpcheckstyle", privileged: false, type: "shell", inline: "/vagrant/phpcheckstyle.sh"
  end
  
  config.vm.provision "shell", inline: "echo 'The VM is ready on 192.168.50.66'", run: "always"
  
end
