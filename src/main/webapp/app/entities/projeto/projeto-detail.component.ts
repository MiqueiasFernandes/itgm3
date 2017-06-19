import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager , JhiLanguageService  } from 'ng-jhipster';

import { Projeto } from './projeto.model';
import { ProjetoService } from './projeto.service';

@Component({
    selector: 'jhi-projeto-detail',
    templateUrl: './projeto-detail.component.html'
})
export class ProjetoDetailComponent implements OnInit, OnDestroy {

    projeto: Projeto;
    private subscription: any;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private jhiLanguageService: JhiLanguageService,
        private projetoService: ProjetoService,
        private route: ActivatedRoute
    ) {
        this.jhiLanguageService.setLocations(['projeto']);
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInProjetos();
    }

    load(id) {
        this.projetoService.find(id).subscribe((projeto) => {
            this.projeto = projeto;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInProjetos() {
        this.eventSubscriber = this.eventManager.subscribe('projetoListModification', (response) => this.load(this.projeto.id));
    }
}
