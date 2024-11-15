# first stage： construct Angular application
FROM node:20 AS build
# set the working directory
WORKDIR /app    
# copy all fils(without in .dockerignore) to working directory
COPY . .
# npm install and build the application
RUN npm install
RUN npm run build

# second stage： Deploy to NginX
FROM nginx:stable-alpine
# from first stage. copy the dist file in build stage to 
COPY --from=build /app/dist/servie-book-sys-web /usr/share/nginx/html
# copy local nginx.conf to replace the default.conf
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80