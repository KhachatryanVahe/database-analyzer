package main

import (
	"fmt"
	"log"
	"os"
	"io"
	"os/exec"
	"bufio"
	// "time"

	// "github.com/gdamore/tcell"
	// "github.com/tj/go-spin"

	"github.com/joho/godotenv"
	"github.com/rivo/tview"

)

var ui = tview.NewApplication()
var form = tview.NewForm()
var output = tview.NewTextView()

func ReadCmdOutput(reader io.ReadCloser, writer io.Writer) {
	buf := bufio.NewReader(reader)
	for {
		line, _, err := buf.ReadLine()
		if err != nil {
			break
		}
		if writer != nil {
			fmt.Fprintf(writer, "%s\n", string(line))
		}

		fmt.Printf("%s\n", string(line))
	}
}

func main() {

	err := godotenv.Load()
	if err != nil {
		log.Fatal("Error loading .env file")
	}

	host := os.Getenv("DB_HOST")
	port := os.Getenv("DB_PORT")
	db := os.Getenv("DB")
	user := os.Getenv("DB_USER")
	password := os.Getenv("DB_PASSWORD")

	log.Print(host)
	log.Print(port)
	log.Print(db)
	log.Print(user)
	log.Print(password)


	form.
		AddInputField("Postgres host", host, 40, nil, func(text string) {
			host = text
		}).
		AddInputField("Postgres port", port, 20, nil, func(text string) {
			port = text
		}).
		AddInputField("Postgres database", db, 20, nil, func(text string) {
			db = text
		}).
		AddInputField("Postgres database user", user, 20, nil, func(text string) {
			user = text
		}).
		AddPasswordField("Postgres database password", password, 20, '*', func(text string) {
			password = text
		}).
		AddButton("next", func() {
			cmd := exec.Command(
				"./run",
			)
			stderr, err := cmd.StderrPipe()
			if err != nil {
				log.Fatal(err)
			}

			stdout, stdouterr := cmd.StdoutPipe()
			if stdouterr != nil {
				log.Fatal(stdouterr)
			}

			if err := cmd.Start(); err != nil {
				log.Fatal(err)
			}

			go ReadCmdOutput(stderr, output)
			go ReadCmdOutput(stdout, output)
		}).
		AddButton("quit", func() {
			ui.Stop()
		})

	if err := ui.SetRoot(form, true).Run(); err != nil {
		panic(err)
	}

}
