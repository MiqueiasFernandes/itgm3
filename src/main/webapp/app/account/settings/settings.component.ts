import { Component, OnInit } from '@angular/core';
import { JhiLanguageService } from 'ng-jhipster';

import { Principal, AccountService, JhiLanguageHelper } from '../../shared';
import {CustomizeService} from '../../entities/customize/customize.service';

@Component({
    selector: 'jhi-settings',
    templateUrl: './settings.component.html'
})
export class SettingsComponent implements OnInit {
    error: string;
    success: string;
    settingsAccount: any;
    languages: any[];
    file: any;
    onLoad = false;
    showMenu = false;

    constructor(
        private account: AccountService,
        private principal: Principal,
        private languageService: JhiLanguageService,
        private languageHelper: JhiLanguageHelper,
        private customizeService: CustomizeService,
    ) {
        this.languageService.setLocations(['settings']);
        this.customizeService.getDesktop().subscribe((desktop) => {
            this.showMenu = desktop.entidades;
        });
    }

    ngOnInit() {
        this.principal.identity().then((account) => {
            this.settingsAccount = this.copyAccount(account);
        });
        this.languageHelper.getAll().then((languages) => {
            this.languages = languages;
        });
    }

    save() {
        this.account.save(this.settingsAccount).subscribe(() => {
            this.error = null;
            this.success = 'OK';
            this.principal.identity(true).then((account) => {
                this.settingsAccount = this.copyAccount(account);
            });
            this.languageService.getCurrent().then((current) => {
                if (this.settingsAccount.langKey !== current) {
                    this.languageService.changeLanguage(this.settingsAccount.langKey);
                }
            });
        }, () => {
            this.success = null;
            this.error = 'ERROR';
        });
    }

    copyAccount(account) {
        return {
            activated: account.activated,
            email: account.email,
            firstName: account.firstName,
            langKey: account.langKey,
            lastName: account.lastName,
            login: account.login,
            imageUrl: account.imageUrl
        };
    }
    setFile($event) {
        this.file = $event.target.files[0];
        this.account.sendImage(this.file)
            .map((res) => res.json())
            .subscribe(
                (response) => {
                    console.log('image: ' + response.image);
                    this.settingsAccount.imageUrl = response.image;
                    this.onLoad = false;
                },
                (error) => {
                    this.onLoad = false;
                });
        this.onLoad = true;
    }

    alterarMenu() {
        this.customizeService.setMenuEntidades(this.showMenu = !this.showMenu);
    }
}
