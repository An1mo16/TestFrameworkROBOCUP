#!/usr/bin/env ruby
if ARGV[0] == "start"
	if RUBY_PLATFORM.downcase.include?("w32") or RUBY_PLATFORM.downcase.include?("mswin")
		Dir.chdir(ENV['RCSSSERVER3D_DIR'] + "/bin")
		IO.popen('rcssserver3d.cmd')
		sleep(1)
		IO.popen('rcssmonitor3d.cmd')
	elsif RUBY_PLATFORM.downcase.include?("linux")
		IO.popen('rcssserver3d')
		sleep(1)
		IO.popen('rcssmonitor3d')
	elsif RUBY_PLATFORM.downcase.include?("darwin")
		puts "OS X support not yet implemented"
	end
elsif ARGV[0] == "stop"
	if RUBY_PLATFORM.downcase.include?("w32") or RUBY_PLATFORM.downcase.include?("mswin")
		system('taskkill /F /IM rcssmonitor3d.exe')
		system('taskkill /F /IM rcssserver3d.exe')
	elsif RUBY_PLATFORM.downcase.include?("linux")
		system('killall rcssmonitor3d')
		system('killall rcssserver3d');
	elsif RUBY_PLATFORM.downcase.include?("darwin")
		puts "OS X support not yet implemented"
	end
else
	puts "Usage: RunServer.rb [start|stop]"
end
