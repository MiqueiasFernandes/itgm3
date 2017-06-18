import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { ItgmTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ModeloExclusivoDetailComponent } from '../../../../../../main/webapp/app/entities/modelo-exclusivo/modelo-exclusivo-detail.component';
import { ModeloExclusivoService } from '../../../../../../main/webapp/app/entities/modelo-exclusivo/modelo-exclusivo.service';
import { ModeloExclusivo } from '../../../../../../main/webapp/app/entities/modelo-exclusivo/modelo-exclusivo.model';

describe('Component Tests', () => {

    describe('ModeloExclusivo Management Detail Component', () => {
        let comp: ModeloExclusivoDetailComponent;
        let fixture: ComponentFixture<ModeloExclusivoDetailComponent>;
        let service: ModeloExclusivoService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ItgmTestModule],
                declarations: [ModeloExclusivoDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ModeloExclusivoService,
                    EventManager
                ]
            }).overrideComponent(ModeloExclusivoDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ModeloExclusivoDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ModeloExclusivoService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new ModeloExclusivo(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.modeloExclusivo).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
