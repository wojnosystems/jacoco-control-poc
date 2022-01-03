RSpec.configure do |config|
  config.after(:each) do |example|
    test_name = example.full_description.gsub(/\W/, "_")
    `java -jar ../jacoco/jacococli.jar dump --address localhost --port 6300 --destfile '#{test_name}.exec' --reset`
  end
end
