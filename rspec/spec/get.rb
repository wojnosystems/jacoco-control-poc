require 'net/http'
require 'uri'

BASE_URL = "http://#{ENV["WEB_HOST"] || "localhost"}:#{ENV["WEB_PORT"] || "8080"}"

def get(path)
    uri_str = BASE_URL + path
    response = Net::HTTP.get_response(URI(uri_str))
end
