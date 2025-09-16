# Script PowerShell pour lancer les tests E2E avec couverture
Write-Host "Démarrage de l'application avec couverture..." -ForegroundColor Green

# Démarrer l'application avec couverture en arrière-plan
$job = Start-Job -ScriptBlock {
    Set-Location "C:\Users\Emma\IdeaProjects\Testez-une-application-full-stack\front"
    npm run serve-coverage
}

# Attendre que l'application démarre
Write-Host "Attente du démarrage de l'application..." -ForegroundColor Yellow
Start-Sleep -Seconds 15

# Lancer les tests E2E
Write-Host "Lancement des tests E2E..." -ForegroundColor Green
npx cypress run --browser chrome --headless

# Arrêter l'application
Write-Host "Arrêt de l'application..." -ForegroundColor Yellow
Stop-Job $job
Remove-Job $job

# Générer le rapport de couverture
Write-Host "Génération du rapport de couverture..." -ForegroundColor Green
npm run e2e:coverage

Write-Host "Terminé !" -ForegroundColor Green
