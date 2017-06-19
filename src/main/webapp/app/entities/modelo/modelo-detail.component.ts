import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager , JhiLanguageService  } from 'ng-jhipster';

import { Modelo } from './modelo.model';
import { ModeloService } from './modelo.service';

@Component({
    selector: 'jhi-modelo-detail',
    templateUrl: './modelo-detail.component.html'
})
export class ModeloDetailComponent implements OnInit, OnDestroy {

    modelo: Modelo;
    private subscription: any;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private jhiLanguageService: JhiLanguageService,
        private modeloService: ModeloService,
        private route: ActivatedRoute
    ) {
        this.jhiLanguageService.setLocations(['modelo']);
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInModelos();
    }

    load(id) {
        this.modeloService.find(id).subscribe((modelo) => {
            this.modelo = modelo;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInModelos() {
        this.eventSubscriber = this.eventManager.subscribe('modeloListModification', (response) => this.load(this.modelo.id));
    }
}
